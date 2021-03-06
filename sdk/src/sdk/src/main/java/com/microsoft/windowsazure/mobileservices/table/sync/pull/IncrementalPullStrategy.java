package com.microsoft.windowsazure.mobileservices.table.sync.pull;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.table.DateTimeOffset;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceSystemColumns;
import com.microsoft.windowsazure.mobileservices.table.query.Query;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOperations;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marianosanchez on 11/3/14.
 */
public class IncrementalPullStrategy extends PullStrategy {

    private static final String INCREMENTAL_PULL_STRATEGY_TABLE = "__incrementalPullData";

    private MobileServiceLocalStore mStore;
    private DateTimeOffset maxUpdatedAt;
    private DateTimeOffset deltaToken;
    private Boolean hasPreviousResults = true;
    private String queryId;
    private Query originalQuery;

    public IncrementalPullStrategy(Query query, String queryId, MobileServiceLocalStore localStore, MobileServiceJsonTable table) {
        super(query, table);
        this.mStore = localStore;
        this.queryId = queryId;
    }

    public static void initializeStore(MobileServiceLocalStore store) throws MobileServiceLocalStoreException {

        Map<String, ColumnDataType> columns = new HashMap<String, ColumnDataType>();
        columns.put("id", ColumnDataType.String);
        columns.put("maxupdateddate", ColumnDataType.String);

        store.defineTable(INCREMENTAL_PULL_STRATEGY_TABLE, columns);
    }

    public void initialize() {

        JsonElement results = null;

        try {

            query.includeDeleted();
            query.removeInlineCount();
            query.removeProjection();

            originalQuery = query;

            hasPreviousResults = false;

            results = mStore.read(
                    QueryOperations.tableName(INCREMENTAL_PULL_STRATEGY_TABLE)
                            .field("id")
                            .eq(table.getTableName() + "_" + queryId));

            if (results != null) {

                JsonArray resultsArray = results.getAsJsonArray();

                if (resultsArray.size() > 0) {
                    JsonElement result = resultsArray.get(0);

                    String stringMaxUpdatedDate = result.getAsJsonObject()
                            .get("maxupdateddate").getAsString();

                    deltaToken = maxUpdatedAt = getDateFromString(stringMaxUpdatedDate);
                }
            }

            setupQuery(maxUpdatedAt);

        } catch (MobileServiceLocalStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public void onResultsProcessed(JsonArray elements) {

        if (elements.size() == 0) {
            if (maxUpdatedAt != null && hasPreviousResults) {
                saveMaxUpdatedDate(new DateTime(maxUpdatedAt.getTime() + 1).toString());
            }

            return;
        }

        hasPreviousResults = true;

        JsonObject lastElement = elements.get(elements.size() - 1).getAsJsonObject();

        String lastElementUpdatedAt = lastElement.get(MobileServiceSystemColumns.UpdatedAt).getAsString();

        maxUpdatedAt = getDateFromString(lastElementUpdatedAt);

        saveMaxUpdatedDate(lastElementUpdatedAt);
    }

    public boolean moveToNextPage(int lastElementCount) {

        if (deltaToken == null || maxUpdatedAt.after(deltaToken)) {

            if (lastElementCount == 0) {
                return false;
            }

            deltaToken = maxUpdatedAt;

            setupQuery(maxUpdatedAt);

            return true;
        }

        return super.moveToNextPage(lastElementCount);
    }

    private void saveMaxUpdatedDate(String lastElementUpdatedAt) {

        JsonObject updatedElement = new JsonObject();

        updatedElement.addProperty("id", table.getTableName() + "_" + queryId);
        updatedElement.addProperty("maxupdateddate", lastElementUpdatedAt);

        try {
            mStore.upsert(INCREMENTAL_PULL_STRATEGY_TABLE, updatedElement, false);
        } catch (MobileServiceLocalStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupQuery(DateTimeOffset maxUpdatedAt) {

        totalRead = 0;
        this.query = originalQuery.deepClone();

        if (this.query.getOrderBy().size() > 0) {
            this.query.getOrderBy().clear();
        }

        if (maxUpdatedAt != null) {

            Query filterQuery = QueryOperations.field(MobileServiceSystemColumns.UpdatedAt).ge(this.maxUpdatedAt);

            if (!Strings.isNullOrEmpty(this.query.getTableName())) {
                filterQuery.tableName(this.query.getTableName());
            }

            if (this.query.getQueryNode() != null) {
                this.query = this.query.and(filterQuery);
            } else {
                this.query = filterQuery.top(this.query.getTop());
            }
        }

        if (this.query.getTop() == 0) {
            this.query.top(defaultTop);
        } else {
            this.query.top(Math.min(query.getTop(), maxTop));
        }

        this.query.getOrderBy().clear();
        this.query.orderBy(MobileServiceSystemColumns.UpdatedAt, QueryOrder.Ascending);
    }

    private DateTimeOffset getDateFromString(String stringValue) {

        if (stringValue == null) {
            return null;
        }

        DateTime dateTime = new DateTime(stringValue);
        return new DateTimeOffset(dateTime.toDate());
    }
}

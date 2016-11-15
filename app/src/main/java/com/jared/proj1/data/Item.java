package com.jared.proj1.data;

import org.json.JSONObject;

/**
 * Created by Sam on 11/14/2016.
 */
public class Item implements JSONPopulator {
    private Condition mCondition;

    public Condition getCondition() {
        return mCondition;
    }

    @Override
    public void populate(JSONObject data) {
        mCondition = new Condition();
        mCondition.populate(data.optJSONObject("condition"));
    }
}

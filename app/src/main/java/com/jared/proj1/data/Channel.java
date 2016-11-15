package com.jared.proj1.data;

import org.json.JSONObject;

/**
 * Created by Sam on 11/14/2016.
 */
public class Channel implements JSONPopulator {
    private Item mItem;
    private Units mUnits;

    public Units getUnits() {
        return mUnits;
    }

    public Item getItem() {
        return mItem;
    }

    @Override
    public void populate(JSONObject data) {
        mUnits = new Units();
        mUnits.populate(data.optJSONObject("units"));

        mItem = new Item();
        mItem.populate(data.optJSONObject("item"));
    }
}

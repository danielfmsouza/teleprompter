package com.easyapps.singerpro.presentation;

/**
 * Created by daniel on 14/09/2016.
 * Defines callback methods used by Activities
 */
public interface ActivityCallback {
    void showContent();
    void hideContent();
    void removeItem(String itemName);
}
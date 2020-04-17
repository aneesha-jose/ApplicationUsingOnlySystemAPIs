package com.aneeshajose.headlines.base.contract;

/**
 * Created by swapnull on 06/12/16.
 */

public interface BaseView {

    void showProgress();

    void hideProgress();

    void showMessage(String toastMessage, boolean showErrorView);

}

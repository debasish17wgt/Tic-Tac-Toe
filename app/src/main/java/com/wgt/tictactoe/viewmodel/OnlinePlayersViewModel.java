package com.wgt.tictactoe.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

public class OnlinePlayersViewModel extends ViewModel {

    public ObservableBoolean isLoading = new ObservableBoolean();

}

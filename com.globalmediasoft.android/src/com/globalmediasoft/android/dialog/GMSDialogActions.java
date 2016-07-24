package com.globalmediasoft.android.dialog;

import com.globalmediasoft.android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class GMSDialogActions extends DialogFragment {
	protected AlertDialog.Builder builder;
	protected String[] actions;
	protected DialogInterface.OnClickListener listener;
	
	public GMSDialogActions() {
		setArguments(new Bundle());
	}
	
	public GMSDialogActions setActions(String[] actions) {
		this.actions = actions;
		return this;
	}
	
	public GMSDialogActions onItemSelected(DialogInterface.OnClickListener listener) {
		this.listener = listener;
		return this;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().containsKey("title") ? getArguments().getString("title") : getResources().getString(R.string.gmsDialogActions_title))
        	   .setItems(actions, listener);

        return builder.create();
    }
}

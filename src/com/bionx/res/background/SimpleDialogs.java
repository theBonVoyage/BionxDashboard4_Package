package com.bionx.res.background;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

public class SimpleDialogs {

	public interface OnYesNoResponse {
		public void onYesNoResponse(boolean isYes);
	}

	public static void displayYesNoDialog(String yesText, String noText,
			String title, String bodyText, Context context,
			final OnYesNoResponse callback) {
		new AlertDialog.Builder(context)
				.setMessage(bodyText)
				.setTitle(title)
				.setPositiveButton(yesText,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								callback.onYesNoResponse(true);
							}

						})
				.setNeutralButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						callback.onYesNoResponse(false);
					}
				}).show();
	}

	public static void displayOkDialog(String title, String body,
			Context context) {
		new AlertDialog.Builder(context).setMessage(body).setTitle(title)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	public static void displayOkDialog(int titleID, int bodyID, Context context) {
		Resources res = context.getResources();
		new AlertDialog.Builder(context).setMessage(res.getString(bodyID))
				.setTitle(res.getString(titleID))
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	public static void displayOkDialogIcon(String title, String body,
			Context context, int resID) {
		new AlertDialog.Builder(context).setMessage(body).setTitle(title)
				.setIcon(resID)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
}

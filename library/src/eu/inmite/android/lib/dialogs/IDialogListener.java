package eu.inmite.android.lib.dialogs;

public interface IDialogListener {

	void onPositiveButtonClicked(int requestCode);

	void onNegativeButtonClicked(int requestCode);

	void onCancelled(int requestCode);
}

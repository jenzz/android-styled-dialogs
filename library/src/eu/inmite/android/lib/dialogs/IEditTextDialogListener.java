package eu.inmite.android.lib.dialogs;

public interface IEditTextDialogListener {

	void onPositiveButtonClicked(String input, int requestCode);

	void onNegativeButtonClicked(String input, int requestCode);

	void onCancelled(String input, int requestCode);
}

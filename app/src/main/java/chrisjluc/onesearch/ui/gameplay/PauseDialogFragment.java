package chrisjluc.onesearch.ui.gameplay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import chrisjluc.onesearch.R;

public class PauseDialogFragment extends DialogFragment {

    private PauseDialogListener mListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        WordSearchActivity activity = (WordSearchActivity) getActivity();
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_gameplay_dialog, null);
        Button resumeButton = (Button) layout.findViewById(R.id.bResume);
        Button restartButton = (Button) layout.findViewById(R.id.bRestart);
        Button quitButton = (Button) layout.findViewById(R.id.bQuit);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                mListener.onDialogResume();
            }
        });
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                mListener.onDialogRestart();
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                mListener.onDialogQuit();
            }
        });
        TextView scorePauseTextView = (TextView) layout.findViewById(R.id.tvScorePause);
        scorePauseTextView.setText(Integer.toString(activity.getScore()));
        TextView timerPauseTextView = (TextView) layout.findViewById(R.id.tvTimePause);
        timerPauseTextView.setText(Long.toString(activity.getTimeRemaining() / 1000 + 1));
        builder.setView(layout);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mListener.onDialogResume();
        super.onCancel(dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PauseDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PauseDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface PauseDialogListener {
        public void onDialogQuit();

        public void onDialogResume();

        public void onDialogRestart();
    }
}

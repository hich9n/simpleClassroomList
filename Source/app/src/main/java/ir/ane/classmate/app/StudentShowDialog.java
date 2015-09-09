package ir.ane.classmate.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by hp on 8/29/15.
 */
public class StudentShowDialog extends DialogFragment {

    public static StudentShowDialog newInstance(String title) {
        StudentShowDialog frag = new StudentShowDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString("title");

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
        }


        return new AlertDialog.Builder(getActivity())
//                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
//                                ((MainActivity)getActivity()).doPositiveClick();

                                ((CameraFragment)(getActivity().getSupportFragmentManager().findFragmentByTag("camera"))).handleDialog(true,title);
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
//                                ((MainActivity)getActivity()).doNegativeClick();
                                ((CameraFragment)(getActivity().getSupportFragmentManager().findFragmentByTag("camera"))).handleDialog(false,title);
                            }
                        }
                )
                .create();
    }
}
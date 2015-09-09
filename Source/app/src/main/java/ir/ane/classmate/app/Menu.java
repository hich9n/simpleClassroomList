package ir.ane.classmate.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Menu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private Button btnStartStop,btnAddStudents;
    private EditText etClassName;
    private int state = 0;

    public static final int READY = 0;
    public static final int ADDING = 1;
    public static final int FINISHED = 2;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Menu.
     */
    // TODO: Rename and change types and number of parameters
    public static Menu newInstance(String param1, String param2) {
        Menu fragment = new Menu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Menu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment





        View view =  inflater.inflate(R.layout.fragment_menu, container, false);


        etClassName = (EditText) view.findViewById(R.id.etClassName);
        btnStartStop = (Button) view.findViewById(R.id.btnStartStop);
        btnAddStudents = (Button) view.findViewById(R.id.btnAddStudents);
        btnAddStudents.setEnabled(false);

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == READY) {
                    btnStartStop.setText("Finish");
                    btnAddStudents.setEnabled(true);
                    etClassName.setEnabled(false);
                    state = ADDING;

                    prepareClass();

                }
                else if (state == ADDING)
                {
                    btnStartStop.setText("Start");
                    btnAddStudents.setEnabled(false);
                    etClassName.setEnabled(true);
                    String dest = addStudents();
                    Toast.makeText(getActivity(),"فایل در مسیر زیر کپی شد: "   + dest,Toast.LENGTH_LONG).show();
                    state = READY;
                }

            }
        });

        btnAddStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, CameraFragment.newInstance("", ""), "camera").addToBackStack("camera").commit();

            }
        });

        return view;
    }



    public void updateCount() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tvStdudentCount = (TextView) getView().findViewById(R.id.tvStudentscount);
                long studentCount = Student.count(Student.class, null, new String[]{});
                String count = String.valueOf(studentCount);
                tvStdudentCount.setText(count);

            }
        });
    }

    private void prepareClass() {
        Student.deleteAll(Student.class);
        updateCount();
    }

    private String addStudents() {
        final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ClassMate/");

        if (!dir.exists())
            dir.mkdirs();

        File databaseFile  = getActivity().getApplicationContext().getDatabasePath("classMate.db");
        String database = null;
        if (databaseFile.exists())
            database = databaseFile.getAbsolutePath();

        String className = etClassName.getText().toString();
        String currentTimeString = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        String dest = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ClassMate/" + currentTimeString + "-" + className;

//        File destFile = new File(dest);


        try {
            FileWriter fw = new FileWriter(dest);

            List <Student> students = Student.listAll(Student.class);


            fw.append("Student");
            fw.append(" ,");
            fw.append("Time");
//            fw.append(" ,");
            fw.append("\r\n");

            for (Student student:students)
            {
                fw.append(student.getName());
                fw.append(" ,");
                fw.append(student.getTime());
                fw.append("\r\n");

            }
            fw.close();


//            AneUtils.copy(databaseFile,destFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dest;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;

//            getActivity().getActionBar().setTitle("مرکز آموزش گام- سامانه حضور و غیاب");
            ((ActionBarActivity)activity).getSupportActionBar().setTitle("مرکز آموزش گام- سامانه حضور و غیاب");

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

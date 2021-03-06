package net.mwales.youparklikeanahole;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import net.mwales.youparklikeanahole.ParkingJobModel;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ParkingJobModel mPJ;

    private OnFragmentInteractionListener mListener;

    private Button mSubmitButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2)
    {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);



        return fragment;
    }

    public DetailsFragment()
    {
        // Required empty public constructor
        mPJ = new ParkingJobModel();

        Log.d("NPJ", "Details Fragment constructor");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        Log.d("NPJ", "onCreateView called");




        EditText et = (EditText) v.findViewById(R.id.job_description);
        et.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // Do nothing
                Log.d("NPJ", "EditText::beforeTextChanged called");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // Do nothing
                DetailsFragment.this.mPJ.setDescription(s.toString());
                Log.d("NPJ", "User intputted test=" + DetailsFragment.this.mPJ.getDescription());
                Log.d("NPJ", "EditText::onTextChanged called");
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // Do nothing
                Log.d("NPJ", "EditText::afterTextChanged called");
            }
        });

        mSubmitButton = (Button) v.findViewById(R.id.submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date now = new Date();

                DetailsFragment.this.setDate(now);
            }
        });

        if (mPJ.getDate() != null)
        {
            setDate(mPJ.getDate());
        }

        if (!mPJ.getDescription().isEmpty())
        {
            et.setText(mPJ.getDescription());
        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
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
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void setDate(Date submitDate)
    {
        mPJ.setDate(submitDate);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm a");

        mSubmitButton.setText( sdf.format(submitDate) );
    }

}

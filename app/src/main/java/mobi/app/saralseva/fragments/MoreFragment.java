package mobi.app.saralseva.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.BaseApp;
import mobi.app.saralseva.activities.MainActivity;
import mobi.app.saralseva.firebase.FirebaseAppUpdater;
import mobi.app.saralseva.utils.AlertManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;

    private OnFragmentInteractionListener mListener;

    private Spinner spinner;

    private Context ctx;

    private int position;
    private int positionState;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    static final String[] langs = new String[] { "English", "Hindi",
            "Kannada"};
    TextView langSel,termsCond,aboutUs,logoName,versionName;
    ArrayAdapter<String> spinnerArrayAdapter;
    LinearLayout termsLayout;
    LinearLayout aboutusLayout;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        v= inflater.inflate(R.layout.fragment_more, container, false);
        ctx=getActivity();
        sharedPreferences = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        langSel= (TextView) v.findViewById(R.id.textViewLangSelected);
        termsCond= (TextView) v.findViewById(R.id.textViewTermsCond);
        aboutUs= (TextView) v.findViewById(R.id.textViewAboutUs);
        logoName= (TextView) v.findViewById(R.id.textViewSeralSevaName);
        versionName= (TextView) v.findViewById(R.id.textViewSeralSevaVersion);


        Typeface robotoThin = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto-Thin.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto-Light.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto-Regular.ttf");

        langSel.setTypeface(robotoRegular);
        termsCond.setTypeface(robotoRegular);
        aboutUs.setTypeface(robotoRegular);
        logoName.setTypeface(robotoLight);
        versionName.setTypeface(robotoLight);


        termsLayout= (LinearLayout) v.findViewById(R.id.layoutTerms);
        aboutusLayout= (LinearLayout) v.findViewById(R.id.layoutAboutus);


        spinner= (Spinner) v.findViewById(R.id.spinner);
        spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, langs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        versionName.setText(FirebaseAppUpdater.getAppVersion(getActivity()));



        positionState=sharedPreferences.getInt("pos",0);
        spinner.setSelection(positionState);

        spinner.setOnItemSelectedListener(this);

        termsLayout.setOnClickListener(this);
        aboutusLayout.setOnClickListener(this);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pos", position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(ctx, ""+adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();

//        editor.putString("lang",adapterView.getItemAtPosition(i).toString());
        editor.putString("lang",adapterView.getItemAtPosition(i).toString());
        editor.putInt("pos",i);
        if(i==positionState){
            //Toast.makeText(ctx, "no chnage", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(ctx, "chnage strings", Toast.LENGTH_SHORT).show();
           // ctx.startActivity(new Intent(ctx, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            Intent intent = new Intent(ctx,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        editor.commit();
        position=i;


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        switch (view.getId()){
            case R.id.layoutAboutus:
                if(BaseApp.isNetworkAvailable(getActivity())){
                    AboutusFragment aboutusFragment=new AboutusFragment();
                    aboutusFragment.show(fragmentManager,"");
                }else {
                    AlertManager.showAlertDialog(getActivity(),getString(R.string.alert_header_internet),getString(R.string.alert_desc_internet),false);
                }

                break;
            case R.id.layoutTerms:
                if(BaseApp.isNetworkAvailable(getActivity())) {
                    TermsConditionFragment termsConditionFragment = new TermsConditionFragment();
                    termsConditionFragment.show(fragmentManager, "");
                }else{
                    AlertManager.showAlertDialog(getActivity(),getString(R.string.alert_header_internet),getString(R.string.alert_desc_internet),false);
                }
                break;
        }

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

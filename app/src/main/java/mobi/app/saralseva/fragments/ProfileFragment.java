package mobi.app.saralseva.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobi.app.saralseva.R;
import mobi.app.saralseva.models.UserProfile;
import mobi.app.saralseva.utils.PermissionClass;
import mobi.app.saralseva.utils.SharedPreferenceManager;
import mobi.app.saralseva.utils_view.CustomExpandableListAdapter;
import mobi.app.saralseva.utils_view.ExpandableListDataPump;
import mobi.app.saralseva.fragments.SaveFragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    FrameLayout masterFrameLayout;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    ImageView editBtn;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private View v;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ImageView iv_profile;

    private static final int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;
    private String userChoosenTask;
    private int REQUEST_CAMERA=2;

    ExpandableListDataPump expandableListDataPump;
    private UserProfile userProfile;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private ProgressBar progressImage;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        v= inflater.inflate(R.layout.fragment_profile, container, false);
        masterFrameLayout = (FrameLayout) v.findViewById(R.id.masterpage);
        sharedPreferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        iv_profile=(ImageView)v.findViewById(R.id.imgvw_profile);
//        progressImage=(ProgressBar)v.findViewById(R.id.progress_image);

        String s=sharedPreferences.getString("user_image","");
        if(s.length()>0){
            displayImage(Uri.parse(s));

        }


        iv_profile.setOnClickListener(this);



        return v;

    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        editBtn= (ImageView) getView().findViewById(R.id.editButtonProfile);
        editBtn.setOnClickListener(this);
        expandableListView = (ExpandableListView) getView().findViewById(R.id.expandableListView);
        expandableListDataPump=new ExpandableListDataPump();

        //getProfileData();
//        expandableListDetail = ExpandableListDataPump.getData(getActivity());
        expandableListDetail = expandableListDataPump.getData(getActivity());
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getActivity(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
            }
        });



        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getActivity(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });
        expandableListView.setOnFocusChangeListener(new ExpandableListView.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
              if(b){
               //   ViewSwitcher switcher= (ViewSwitcher) view.findViewById(R.id.profileTextSwitcher);
               //   switcher.showPrevious();
              }
            }
        });





        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
           /*     ViewSwitcher switcher= (ViewSwitcher) v.findViewById(R.id.profileTextSwitcher);
                EditText editText= (EditText) v.findViewById(R.id.editTextExpand);
                switcher.showNext();
                if(groupPosition==0) {
                    switch (childPosition) {
                        case 0:

                            editText.setText(sharedPreferences.getString("cName",""));
                            break;
                        case 1:
                            editText.setText("");
                            break;

                        case 2:
                            editText.setText(sharedPreferences.getString("mobileNumber",""));
                            break;
                        case 3:
                            editText.setText("");
                            break;
                    }
                }else if(groupPosition==1){
                    switch (childPosition){
                        case 0:
                            editText.setText("AADFGHK98899");
                            break;
                    }
                }

//                Snackbar.make(
//                        v,
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Snackbar.LENGTH_SHORT
//                ).show();
*/






                return false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        for (int i=0;i<expandableListAdapter.getGroupCount();i++){
            expandableListView.expandGroup(i);
        }

    }



    private UserProfile getProfileData() {
        return SharedPreferenceManager.getInstance(getActivity()).getProfileData();



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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        switch (view.getId()){
            case R.id.editButtonProfile:

                FragmentManager fabfragmentManager=getFragmentManager();
                AddNewVendorFragment addNewVendorFragment=AddNewVendorFragment.newInstance("");
                SaveFragment SaveFragment= mobi.app.saralseva.fragments.SaveFragment.newInstance("","");
                SaveFragment.show(fabfragmentManager,"");
               // fragmentTransaction.replace(R.id.masterpage,SaveFragment);
                fragmentTransaction.commit();

//                FragmentManager fm=getFragmentManager();
//                SaveFragment saveFragment=SaveFragment.newInstance("","");
//                saveFragment.show(fm,"");
                break;

            case R.id.imgvw_profile:
                //showPopup();
                getPermission();


                break;
        }
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= 23){
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                    // Show an expanation to the user *asynchronously* -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//
//                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
//                }
            }else{
                loadImageFromGallery();
            }
        }else {

            loadImageFromGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
     switch (requestCode){
         case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
             // If request is cancelled, the result arrays are empty.
             if (grantResults.length > 0
                     && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                 // permission was granted, yay! Do the
                 // contacts-related task you need to do.
                 loadImageFromGallery();
             } else {

                 // permission denied, boo! Disable the
                 // functionality that depends on this permission.
             }
             return;
         }
     }
    }

    private void showPopup() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Profile photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result= PermissionClass.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                 //   if(result)

                        getImageFromCamera();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                   // if(result)
                        loadImageFromGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();



    }

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    private void loadImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    displayImage(selectedImage);
                    //iv_profile.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    displayImage(selectedImage);

//                    iv_profile.setImageURI(selectedImage);
                    //onCaptureImageResult(data);
                }
                break;



        }

    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iv_profile.setImageBitmap(thumbnail);
    }


    private void displayImage(Uri selectedImage) {
//        Glide.with(getActivity()).load(selectedImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_profile) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                iv_profile.setImageDrawable(circularBitmapDrawable);
//            }
//        });





        Glide.with(this).load(selectedImage)
//                .listener(new RequestListener<Uri, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
//                       progressImage.setVisibility(View.GONE);
//                        return false;
//
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        progressImage.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
                .transform(new CircleTransform(getActivity()))
                .crossFade(1000)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(iv_profile);


//        Picasso.with(getActivity())
//                .load(selectedImage).into(iv_profile);


        editor.putString("user_image",selectedImage.toString());

        editor.commit();


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


    public static class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName();
        }
    }
}

package mobi.app.saralseva.models;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by kumardev on 12/26/2016.
 */

public class RequestSingletonClass {
    private static RequestSingletonClass mInstance;
    private static RequestSingletonClass requestSingletonClass;
    private RequestQueue requestQueue;
    private static Context ctx;

    private RequestSingletonClass(Context context){
        ctx=context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized RequestSingletonClass getInstance(Context context){
        if(mInstance==null){
            mInstance=new RequestSingletonClass(context);
        }
        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }




}

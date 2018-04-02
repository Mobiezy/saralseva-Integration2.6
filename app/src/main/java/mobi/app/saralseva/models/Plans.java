package mobi.app.saralseva.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumardev on 1/27/2017.
 */

public class Plans {

    @SerializedName("Subs_Plan_Details")
   private PlanResponse[] planResponses;


    public PlanResponse[] getPlanResponses() {
        return planResponses;
    }

    public void setPlanResponses(PlanResponse[] planResponses) {
        this.planResponses = planResponses;
    }
}

package mobi.app.saralseva.utils_view;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 1/27/2017.
 */

public class DataPlanDetail {
    String planName;
    String basePrice;
    String tax;
    String total;
    String planInitial;

    public String getPlanInitial() {
        return planInitial;
    }

    public void setPlanInitial(String planInitial) {
        this.planInitial = planInitial;
    }

    public DataPlanDetail(String planName, String basePrice, String tax, String total,String planInitial) {
        this.planName = planName;
        this.basePrice = basePrice;
        this.tax = tax;
        this.total = total;
        this.planInitial=planInitial;
    }

    private List<DataPlanDetail> DataPlanDetails;


    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

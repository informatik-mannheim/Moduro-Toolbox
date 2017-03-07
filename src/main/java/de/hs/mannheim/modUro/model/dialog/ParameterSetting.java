package de.hs.mannheim.modUro.model.dialog;

/**
 * Bean class for Moduro toolbox settings.
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class ParameterSetting {

    private double steadyStateTime = 40.0;
    private double endTime = 720.0;
    private String totalfitnesstype = "LAST";

    private double firstImagePerc = 0.2;
    private double secondImagePerc = 0.5;
    private double thirdImagePerc = 0.95;

    public ParameterSetting() {
    }

    public String getTotalfitnesstype() {
        return totalfitnesstype;
    }

    public void setTotalfitnesstype(String totalfitnesstype) {
        this.totalfitnesstype = totalfitnesstype;
    }

    public double getSteadystatetime() {
        return steadyStateTime;
    }

    public void setSteadystatetime(double steadyStateTime) {
        this.steadyStateTime = steadyStateTime;
    }

    public double getEndtime() {
        return endTime;
    }

    public void setEndtime(double endTime) {
        this.endTime = endTime;
    }

    public double getFirstImage() {
        return firstImagePerc;
    }

    public void setFirstImage(double percentage) {
        this.firstImagePerc = percentage;
    }

    public double getSecondImage() {
        return secondImagePerc;
    }

    public void setSecondImage(double percentage) {
        this.secondImagePerc = percentage;
    }

    public double getThirdImage() {
        return thirdImagePerc;
    }

    public void setThirdImage(double percentage) {
        this.thirdImagePerc = percentage;
    }
}

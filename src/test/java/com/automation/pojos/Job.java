package com.automation.pojos;

import com.google.gson.annotations.SerializedName;

public class Job {

    @SerializedName("job_id") // we have to add this if variable name in java is different from variable name in JSON.
    // Otherwise, Gson will map them automatically if variable name is same as in JSON.
    private String jobId;
    @SerializedName("job_title")
    private String jobTitle; //
    @SerializedName("min_salary")
    private Integer minSalary;
    @SerializedName("max_salary")
    private Integer maxSalary;

    public Job() {
    }

    public Job(String jobId, String jobTitle, Integer minSalary, Integer maxSalary) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Integer minSalary) {
        this.minSalary = minSalary;
    }

    public Integer getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Integer maxSalary) {
        this.maxSalary = maxSalary;
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobId='" + jobId + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                '}';
    }
}

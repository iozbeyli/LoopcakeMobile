package com.loopcake.loopcakemobile.LCList.LCListItems;

/**
 * Created by Melih on 22.05.2017.
 */

public class ChecklistItem {
    private int point;

    public int getPoint() {
        return point;
    }

    private String _id;
    private String label;

    public String getId() {
        return _id;
    }

    private String cpid;

    public String getcpid() {
        return cpid;
    }

    private Boolean status;

    public Boolean getStatus(){
        return status;
    }

    public void toggleStatus(){
        status = !status;
    }

    public String getDetail() {
        return detail;
    }

    private String detail;

    public ChecklistItem(int point,boolean status, String _id, String cpid, String detail, String label){
        this.point = point;
        this.status = status;
        this._id = _id;
        this.cpid = cpid;
        this.detail = detail;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "ChecklistItem{" +
                "point=" + point +
                ", _id='" + _id + '\'' +
                ", label='" + label + '\'' +
                ", cpid='" + cpid + '\'' +
                ", status=" + status +
                ", detail='" + detail + '\'' +
                '}';
    }
}

package com.loopcake.loopcakemobile.LCList.LCListItems;

/**
 * Created by Melih on 22.05.2017.
 */

public class ChecklistItem {
    private String detail;

    public String getDetail() {
        return detail;
    }

    private String _id;

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

    public ChecklistItem(String detail,boolean status, String _id, String cpid){
        this.detail = detail;
        this.status = status;
        this._id = _id;
        this.cpid = cpid;
    }
}

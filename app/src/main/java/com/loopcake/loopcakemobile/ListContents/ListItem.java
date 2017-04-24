package com.loopcake.loopcakemobile.ListContents;

/**
 * Created by Melih on 24.04.2017.
 */

public class ListItem {


    private String visibleContent;

    public String getSecretContent() {
        return secretContent;
    }

    public void setSecretContent(String secretContent) {
        this.secretContent = secretContent;
    }

    private String secretContent;

    public String getVisibleContent(){
        return visibleContent;
    }

    public void setVisibleContent(String visibleContent) {
        this.visibleContent = visibleContent;
    }

    public ListItem(String visibleContent,String secretContent){
        this.visibleContent = visibleContent;
        this.secretContent = secretContent;
    }


}

package com.example.fyp2022;

public class AwarenessModal {
    private final int awareID;
    private final String awareTag;
    private final String awareDate;
    private final String imageUrl;
    private final String awareTitle;
    private final String awareDesc;
    private final String awareLink;

    public AwarenessModal(int awareID, String awareTag, String awareDate, String imageUrl, String awareTitle, String awareDesc, String awareLink) {
        this.awareID = awareID;
        this.awareTag = awareTag;
        this.awareDate = awareDate;
        this.imageUrl = imageUrl;
        this.awareTitle = awareTitle;
        this.awareDesc = awareDesc;
        this.awareLink = awareLink;
    }

    public int getAwareID() {
        return awareID;
    }
    public String getAwareTag() {
        return awareTag;
    }
    public String getAwareDate() {
        return awareDate;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getAwareTitle() {
        return awareTitle;
    }
    public String getAwareDesc() {
        return awareDesc;
    }
    public String getAwareLink() {return awareLink;}

    @Override
    public String toString() {
        return "AwarenessModal{" +
                "awareID=" + awareID +
                ", awareTag='" + awareTag + '\'' +
                ", awareDate='" + awareDate + '\'' +
                ", ImageUrl='" + imageUrl + '\'' +
                ", awareTitle='" + awareTitle + '\'' +
                ", awareDesc='" + awareDesc + '\'' +
                ", awareLink='" + awareLink + '\'' +
                '}';
    }
}

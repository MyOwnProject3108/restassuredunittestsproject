import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.response.Response;

public class ResponseTabHelper {

    @JsonProperty("default")
    private String isDefault;

    private String createByUserId;

    @JsonProperty("_id")
    private String tabId;

    private String name;

    private String businessUnitId;


    @JsonProperty("public")
    private String isPublic;

    private String tabType;

    public String getDefault ()
    {
        return isDefault;
    }

    public void setDefault (String isDefault)
    {
        this.isDefault = isDefault;
    }

    public String getCreateByUserId ()
    {
        return createByUserId;
    }

    public void setCreateByUserId (String createByUserId)
    {
        this.createByUserId = createByUserId;
    }

    public String getTabId ()
    {
        return tabId;
    }

    public void setTabId (String tabId)
    {
        this.tabId = tabId;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getBusinessUnitId ()
    {
        return businessUnitId;
    }

    public void setBusinessUnitId (String businessUnitId)
    {
        this.businessUnitId = businessUnitId;
    }

    public String getPublic ()
    {
        return isPublic;
    }

    public void setPublic (String isPublic)
    {
        this.isPublic = isPublic;
    }

    public String getTabType ()
    {
        return tabType;
    }

    public void setTabType (String tabType)
    {
        this.tabType = tabType;
    }


}

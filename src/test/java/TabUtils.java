import com.fasterxml.jackson.annotation.JsonProperty;

public class TabUtils {


        @JsonProperty("default")
       public Boolean isDefault;

        private String name;

        @JsonProperty("public")
        private Boolean isPublic;

        private String tabType;

        public Boolean getDefault ()
    {
        return isDefault;
    }

    public Boolean setDefault (Boolean isDefault)
    {
       return this.isDefault = isDefault;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Boolean getPublic ()
    {
        return isPublic;
    }

    public void setPublic (Boolean isPublic)
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

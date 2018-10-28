package io.yetanotherwhatever.ocpv2;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CodingProblem {

    private String guid;
    private String name;
    private String landingPageUrl;
    private String expirationDate;


    public String getGuid() {
        return guid;
    }

    public CodingProblem setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public String getName() {
        return name;
    }

    public CodingProblem setName(String name) {
        this.name = name;
        return this;
    }

    public String getLandingPageUrl() {
        return landingPageUrl;
    }

    public CodingProblem setLandingPageUrl(String landingPageUrl) {
        this.landingPageUrl = landingPageUrl;
        return this;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public CodingProblem setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof CodingProblem)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        CodingProblem c = (CodingProblem) o;

        // Compare the data members and return accordingly
        return new EqualsBuilder()
                .append(this.getGuid(), c.getGuid())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31)
                .append(getGuid())
                .toHashCode();
    }
}

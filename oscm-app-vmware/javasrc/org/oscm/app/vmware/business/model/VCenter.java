/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2018
 *
 *  Creation Date: 2016-05-24
 *
 *******************************************************************************/

package org.oscm.app.vmware.business.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class VCenter {
    public List<Datacenter> datacenter;
    public String name;
    public String identifier;
    private String url;
    private String userid;
    private String password;
    public int tkey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
  
  
    public boolean paramsNotNullOrEmpty() {
        return !StringUtils.isAnyEmpty(url, userid, password);
    }

}

/**
 * *****************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2018
 *
 * <p>Creation Date: Apr 17, 2012
 *
 * <p>*****************************************************************************
 */
package org.oscm.ui.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.lang3.StringUtils;
import org.oscm.internal.vo.VOMarketplace;
import org.oscm.ui.common.MarketplacesComparator;

@ViewScoped
@ManagedBean(name = "gotoMarketplaceBean")
public class GotoMarketplaceBean extends BaseBean implements Serializable {

  private static final long serialVersionUID = 6745716919639233847L;

  private List<SelectItem> cachedMarketplaces;
  private String selectedMarketplace;

  public String getSelectedMarketplace() {
    return selectedMarketplace;
  }

  public void setSelectedMarketplace(String selectedMarketplace) {
    this.selectedMarketplace = selectedMarketplace;
  }

  /** @return all marketplaces which are owned and to which the supplier can publish */
  public List<SelectItem> getMarketplaces() {
    if (cachedMarketplaces == null) {
      cachedMarketplaces = new ArrayList<SelectItem>();
      cachedMarketplaces = convertToUIModel(loadMarketplaces());
    }
    return cachedMarketplaces;
  }

  Set<VOMarketplace> loadMarketplaces() {

    String tenantId = getIdService().getCurrentUserDetails().getTenantId();

    Set<VOMarketplace> marketplaces = new HashSet<VOMarketplace>();

    if (isLoggedInAndMarketplaceOwner()) {
      List<VOMarketplace> marketplacesOwned = getMarketplaceService().getMarketplacesOwned();

      prepareMarketplaces(marketplaces, marketplacesOwned, tenantId);
    }

    if (isLoggedInAndVendorManager()) {
      List<VOMarketplace> marketplacesForOrganization =
          getMarketplaceService().getMarketplacesForOrganization();

      prepareMarketplaces(marketplaces, marketplacesForOrganization, tenantId);
    }

    return marketplaces;
  }

  void prepareMarketplaces(
      Set<VOMarketplace> marketplacesToDisplay, List<VOMarketplace> marketplaces, String tenantId) {

    List<VOMarketplace> restrictedMarketplaces =
        getMarketplaceService().getRestrictedMarketplaces();

    for (VOMarketplace marketplace : marketplaces) {

      if (!validateMarketplaceTenant(marketplace, tenantId)) {
        continue;
      }
      if (!marketplace.isRestricted()) {
        marketplacesToDisplay.add(marketplace);
      } else if (restrictedMarketplaces.contains(marketplace)) {
        marketplacesToDisplay.add(marketplace);
      }
    }
  }

  boolean validateMarketplaceTenant(VOMarketplace marketplace, String tenantId) {
    return StringUtils.equals(marketplace.getTenantId(), tenantId);
  }

  private List<SelectItem> convertToUIModel(Set<VOMarketplace> marketplaces) {
    List<SelectItem> uiMarketplaces = new ArrayList<SelectItem>();
    List<VOMarketplace> mpList = new ArrayList<VOMarketplace>(marketplaces);

    Collections.sort(mpList, new MarketplacesComparator());
    for (VOMarketplace mp : mpList) {
      uiMarketplaces.add(new SelectItem(mp.getMarketplaceId(), getLabel(mp)));
    }
    return uiMarketplaces;
  }

  private String getLabel(VOMarketplace marketplace) {
    return marketplace.getName() + "(" + marketplace.getMarketplaceId() + ")";
  }

  /** updates the session's mid attribute and forwards to the selected marketplace */
  public String gotoMarketplace() {
    setMarketplaceId(selectedMarketplace);
    return OUTCOME_SUCCESS;
  }

  /*
   * value change listener for marketplace chooser
   */
  public void processValueChange(AjaxBehaviorEvent event) {
    selectedMarketplace = (String) ((UIOutput) event.getSource()).getValue();
  }

  public boolean isButtonEnabled() {
    return getSelectedMarketplace() != null && getSelectedMarketplace().length() > 0;
  }
}

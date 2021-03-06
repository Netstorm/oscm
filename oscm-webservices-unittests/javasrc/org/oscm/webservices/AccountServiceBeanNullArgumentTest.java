/*******************************************************************************
 *  Copyright FUJITSU LIMITED 2018
 *******************************************************************************/

package org.oscm.webservices;

import static org.mockito.Mockito.mock;

import org.oscm.accountservice.bean.AccountServiceBean;
import org.oscm.test.NullArgumentTestBase;
import org.oscm.test.ejb.TestContainer;
import org.oscm.webservices.logger.WebServiceLogger;
import org.oscm.intf.AccountService;

public class AccountServiceBeanNullArgumentTest extends
        NullArgumentTestBase<AccountService> {

    public AccountServiceBeanNullArgumentTest() {
        super(AccountService.class);
        addNullAllowed("registerKnownCustomer", "organizationProperties");
        addNullAllowed("registerCustomer", "paymentInfo");
        addNullAllowed("registerCustomer", "password");
        addNullAllowed("registerCustomer", "serviceKey");
        addNullAllowed("registerCustomer", "marketplaceId");
        addNullAllowed("registerCustomer", "sellerId");
        addNullAllowed("savePaymentConfiguration", "customerConfigurations");
        addNullAllowed("savePaymentConfiguration", "serviceConfigurations");
        addNullAllowed("updateAccountInformation", "organization");
        addNullAllowed("updateAccountInformation", "user");
        addNullAllowed("updateAccountInformation", "marketplaceId");
        addNullAllowed("updateAccountInformation", "imageResource");
        addNullAllowed("getSeller", "sellerId");
        addNullAllowed("getSeller", "locale");
        addNullAllowed("updateOrganization", "imageResource");
        addNullAllowed("savePaymentConfiguration", "defaultConfiguration");
        addNullAllowed("saveUdaDefinitions", "udaDefinitionsToSave");
        addNullAllowed("saveUdaDefinitions", "udaDefinitionsToDelete");
        addNullAllowed("saveUdas", "udas");
    }

    @Override
    protected AccountService createInstance(TestContainer container)
            throws Exception {
        container.enableInterfaceMocking(true);
        final org.oscm.intf.AccountService service = new AccountServiceWS();
        ((AccountServiceWS) service).WS_LOGGER = mock(WebServiceLogger.class);
        ((AccountServiceWS) service).delegate = new AccountServiceBean();
        container.addBean(service);
        return service;
    }

}

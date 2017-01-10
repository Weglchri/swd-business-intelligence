package at.fhj.swd.QueryTest;

import at.fhj.swd.BusinessIntelligence.Company;
import at.fhj.swd.BusinessIntelligenceRepositories.CompanyRepository;
import at.fhj.swd.BusinessIntelligence.Location;
import at.fhj.swd.Helper.JdbcHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCompanyQuery extends JdbcHandler {

    private static Location testAddress;
    private static Location testAddress1;
    private static Location testAddress2;
    private static Company testCompany;
    private static Company testCompany1;
    private static Company testCompany2;
    private static List<Company> companyBranchSearch;
    private static List<Company> companyBranchSearch1;

    static final String company_name = "Stahl Incorporation";
    static final String branch = "Stahlbau";

    static final String company_name1 = "Orange Incorporation GmbH";
    static final String branch1 = "Bergbau";

    static final String company_name2 = "Diamond Mine Coorporation GmbH";
    static final String branch2 = "Bergbau";


    @BeforeClass
    public static void setup() {
        JdbcHandler.build();
        JdbcHandler.init();
    }

    @AfterClass
    public static void teardown() {
        JdbcHandler.close();
        JdbcHandler.destroy();
    }

    @Test
    public void A_create() {
        transaction.begin();

        testAddress = new Location(TestLocationQuery.address, TestLocationQuery.country, TestLocationQuery.zip, TestLocationQuery.city);
        assertNotNull(testAddress);
        manager.persist(testAddress);

        testAddress1 = new Location(TestLocationQuery.address1, TestLocationQuery.country1, TestLocationQuery.zip1, TestLocationQuery.city1);
        assertNotNull(testAddress1);
        manager.persist(testAddress1);

        testAddress2 = new Location(TestLocationQuery.address2, TestLocationQuery.country2, TestLocationQuery.zip2, TestLocationQuery.city2);
        assertNotNull(testAddress2);
        manager.persist(testAddress2);

        testCompany = new Company(company_name, branch, testAddress);
        assertNotNull(testCompany);
        manager.persist(testCompany);

        testCompany1 = new Company(company_name1, branch1, testAddress1);
        assertNotNull(testCompany1);
        manager.persist(testCompany1);

        testCompany2 = new Company(company_name2, branch2, testAddress2);
        assertNotNull(testCompany2);
        manager.persist(testCompany2);

        transaction.commit();
    }

    @Test
    public void B_repoTest() {
        CompanyRepository companyRepo1 = new CompanyRepository(manager);
        Company testCompany1 = companyRepo1.findByName(company_name);
        CompanyRepository companyRepo2 = new CompanyRepository(manager);
        Company testCompany2 = companyRepo2.findByName(company_name1);
        CompanyRepository companyRepo3 = new CompanyRepository(manager);
        Company testCompany3 = companyRepo3.findByName(company_name2);

        assertEquals(testCompany1.getBranch(), TestCompanyQuery.testCompany.getBranch());
        assertEquals(testCompany1.getCompany(), TestCompanyQuery.testCompany.getCompany());
        assertEquals(testCompany1.getAddress(), TestCompanyQuery.testCompany.getAddress());

        assertEquals(testCompany2.getBranch(), TestCompanyQuery.testCompany1.getBranch());
        assertEquals(testCompany2.getCompany(), TestCompanyQuery.testCompany1.getCompany());
        assertEquals(testCompany2.getAddress(), TestCompanyQuery.testCompany1.getAddress());

        assertEquals(testCompany3.getBranch(), TestCompanyQuery.testCompany2.getBranch());
        assertEquals(testCompany3.getCompany(), TestCompanyQuery.testCompany2.getCompany());
        assertEquals(testCompany3.getAddress(), TestCompanyQuery.testCompany2.getAddress());
    }

    @Test
    public void C_repoTestGetBranche() {
        CompanyRepository companyRepo = new CompanyRepository(manager);
        companyBranchSearch = companyRepo.findByBranch(branch);
        companyBranchSearch1 = companyRepo.findByBranch(branch1);

        assertEquals(1, companyBranchSearch.size());
        assertEquals(2, companyBranchSearch1.size());
    }

    @Test
    public void D_remove() {
        transaction.begin();

        Company testCompany = manager.find(Company.class, company_name);
        Company testCompany1 = manager.find(Company.class, company_name1);
        assertNotNull(testCompany);
        assertNotNull(testCompany1);

        Location testAddress = manager.find(Location.class, TestLocationQuery.address);
        Location testAddress1 = manager.find(Location.class, TestLocationQuery.address1);
        assertNotNull(testAddress);
        assertNotNull(testAddress1);

        manager.remove(testCompany);
        manager.remove(testCompany1);
        manager.remove(testAddress);
        manager.remove(testAddress1);

        transaction.commit();

        testAddress = manager.find(Location.class, TestLocationQuery.address);
        testAddress1 = manager.find(Location.class, TestLocationQuery.address1);
        assertEquals(null, testAddress);
        assertEquals(null, testAddress1);

        testCompany = manager.find(Company.class, company_name);
        testCompany1 = manager.find(Company.class, company_name1);
        assertNull(testAddress);
        assertNull(testAddress1);
        assertNull(testCompany);
        assertNull(testCompany1);
    }

}
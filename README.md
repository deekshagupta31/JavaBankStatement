# JavaBankStatement
Provided with Account Number date range and amount range, return the statement from database

package- com.assignment

Account.java and Statement.java are classes with getter setter for the provided tables.
Statement has Autowired Account object
StatementController is the Controller entry point with the RequestMapping provided as 'myAccount' . The method returnDetails() has requestMapping 'statementDetails' and              Requestethod POST. This method provides the statement details from the provided data present in map passed through @RequestParams. The retun type is ResponseEntity.
   First, authorization is checked for username. If not authorized then 401 status response given
   Second, if authorization accepted the validation for the input fields done.
   This is done using Utils.java class(details below)
   If validation failed then response given with status OK but Message as Validation failed. In key Errors list of fails provided for user reference.
   If successful vaidation then StatementDetails.java class called for generation of statement details and returned map send in response.
   If non of the able occurs then status Bad Request send.
StatementDetails.java class has a method to generate statements from the given data.
  Method returnStatements() takes HashMap<String,Object> and returns LinkedHashMap <String,Object>.
  First,Start Date and End Date are converted to LocalDate; if no Start date is provided it is set to current date. If no end date is provided then it is set to 3 months back from   start date.
  Query is formed to be executed in MS Access database. 
  For MS Access connection Data Source is mydsn user is admin and password is password;
  In query account-number from Account table converted to unsigned integer for join; dates converted to format 'yyyy-MM-dd' and amount converted to integer(all done for proper       execution of the query and use of BETWEEN and operators)
  Query executed using PreparedStatement and ResultSet generated.
  If no records found in ResultSet the map with key "Message" and value "No Records Found" returned
  If records are generated then "Message" is "Success" and Statements are present as values having Sequence as keys.
Utils.java class is a generic class provided with currently two method fro validaion using pattern. One for Number and other for dat format.
ApplicationContext.xml -  enable component scanning Annotation And Package Specification
In WEB-INF view folder will contain the js files(currently empty folder)
spring-mvc-demo-servlet.xml and web.xml has InternalResourceViewResolver, DispatcherServlet mapping
   



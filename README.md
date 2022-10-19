
<h2 align="center">
	Reddit newsletter
 </h2>
 <p>
 Tech stack: Java, Spring Boot, MongoDB, Thymeleaf, JUnit
</p>
 <p>
 Email based newsletter app made for learning purposes.
 </p>
<br>
<h3 align="center">
What it does?
</h3>
<p>
Reddit newsletter fetches posts data from public reddit API and sends out daily emails to all subscribed email adresses.
</p>
<br>
<h3 align="center">
Can i run it?
</h3>
<p>
If you want to run it you will have to:

1. Provide your own MongoDB database - easiest done with free MongoDB cluster.
2. Get application id and secret from reddit to access the API - https://github.com/reddit-archive/reddit/wiki/OAuth2
3. Update properties in application.properties
4. (Optional) Change SMTP server - By default, SMTP server is set to a free testing service - https://www.wpoven.com/tools/free-smtp-server-for-testing
</p>

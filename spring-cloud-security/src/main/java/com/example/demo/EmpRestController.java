package com.example.demo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@EnableOAuth2Sso
public class EmpRestController extends WebSecurityConfigurerAdapter {

	Map<Integer, Employee> empMap = new HashMap<>();

	@Autowired
	private OAuth2ClientContext clientContext;

	@RequestMapping(value = "/emp", method = RequestMethod.GET)
	public Collection<Employee> getEmployees() {

		if (ObjectUtils.isEmpty(empMap)) {

			empMap.put(123, new Employee(123, "Atul", 40, "Maharastra"));
			empMap.put(124, new Employee(124, "Shushant", 32, "Maharastra"));
			empMap.put(125, new Employee(124, "Ankur", 23, "Maharastra"));

		}
		return empMap.values();
	}

	@RequestMapping(value = "/emp/{empid}", method = RequestMethod.GET)
	public Collection<Employee> saveEmployees(@PathVariable Integer empid) {
		empMap.put(empid, new Employee(empid, "Bala", 45, "karl"));
		return empMap.values();
	}

	@RequestMapping("/acc_token")
	public String getToken() {

		String token = clientContext.getAccessToken().getValue();
		System.out.println("access_token :" + token);
		return token;
	}

	@RequestMapping(value = "/report/{reportid}", method = RequestMethod.GET)
	public String reportdata(@PathVariable Integer reportid) {
		final String uri = "https://apimgroupmpro01.azure-api.net/MetisClients/api/Clients";

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);

		System.out.println(result);

		return result;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/", "/emp").permitAll().anyRequest().authenticated();
	}

}

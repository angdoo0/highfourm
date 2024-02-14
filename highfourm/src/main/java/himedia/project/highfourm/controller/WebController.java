package himedia.project.highfourm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

	@GetMapping(value = {"/error", "/users", "/users/search", "/bom", "/bom/detail/{productId}",
			"/materials/stock", "/materials/stock/search", "/materials/order-history", "/materials/order-history/search", "/materials/order-history/new",
			"/materials/order-history/edit/{orderHistoryId}", "/mrp", "/mrp/{productionPlanId}", "/mrp/search", "/mrp/{productionPlanId}/search",
			"/orders", "/orders/search","/orders/new", "/production-performance", "/production-performance/search","/production-performance/{productionPlanId}/chart","/production-performance/{productionPlanId}/controll-chart", "/production-status",
			"/production-plan","/production-plan/search", "/production-plan/{productionPlanId}", "/work-performance", "/work-performance/search", "/work-performance/new"})
	public String forward() {
		return "forward:/index.html";
	}
	
}

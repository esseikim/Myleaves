package com.itwill.myleaves.web.mypage;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwill.myleaves.dto.order.TotalOrderUpdateDto;
import com.itwill.myleaves.repository.orderDetail.OrderDetail;
import com.itwill.myleaves.repository.store.Store;
import com.itwill.myleaves.repository.store.StoreWish;
import com.itwill.myleaves.repository.totalOrder.TotalOrder;
import com.itwill.myleaves.service.store.MypageStoreService;
import com.itwill.myleaves.service.store.StoreService;
import com.itwill.myleaves.service.totalOrder.TotalOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// 지현 

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/store")
public class MyPageStoreController {
	private final MypageStoreService myPageStoreService;
	private final TotalOrderService orderService;
	private final StoreService storeService;
	
	@PreAuthorize("hasRole('MEMBER')")
	@GetMapping("/storeWish")
	public void wishList(StoreWish storeWishList, Model model) {
//		log.info("read()");
		
		List<Store> list = myPageStoreService.read(storeWishList);
		Map<Long, String> thumbnails = new HashMap<>();
		for(Store store: list){
			thumbnails.put(store.getItemId(), Base64.getEncoder().encodeToString(store.getThumbnail()));
        }
        model.addAttribute("images", thumbnails);
		model.addAttribute("wishStore", list);
	
	}
	
	@PreAuthorize("hasRole('MEMBER')")
	@GetMapping("/orderList")
	public void orderList(String userId, Model model) {
//		log.info("read()");
		List<TotalOrder> list = orderService.read(userId);
		
		model.addAttribute("totalOrders", list);
	}
	
	@PreAuthorize("hasRole('MEMBER')")
	@GetMapping("/orderDetail")
	public void orderDetail(Long orderId, Model model) {
		TotalOrder order = orderService.read(orderId);
		
		// itemId로 찾아줄것
		Map<Long ,Store> storeList = new HashMap<>();
		List<OrderDetail> detailList = orderService.readOrderDetails(order.getOrderId());
		
		Map<String, Long> prices = new HashMap<>();
		
		// 총 결제 금액
		Long totalDetail = (long) 0;
		for(OrderDetail details: detailList) {
			totalDetail += details.getPrice();
			storeList.put(details.getItemId(), storeService.read(details.getItemId()));
		}
		prices.put("totalDetail", totalDetail);
		
		// 배송비
		Long deliveryPrice = order.getPrice() - totalDetail;
		prices.put("deliveryPrice", deliveryPrice);
		
		Map<Long, String> thumbnails = new HashMap<>();
		for(OrderDetail detail: detailList){
			thumbnails.put(detail.getItemId(), Base64.getEncoder().encodeToString(storeService.read(detail.getItemId()).getThumbnail()));
        }
        model.addAttribute("images", thumbnails);
		model.addAttribute("prices", prices);
		model.addAttribute("order", order);
		model.addAttribute("details", detailList);
		model.addAttribute("items", storeList);
	}
	
	@PreAuthorize("hasRole('MEMBER')")
	@PutMapping("/orderDetail/update/{orderId}")
	public ResponseEntity<String> update(@PathVariable long orderId, @RequestBody TotalOrderUpdateDto dto){
		// log.info("update(dto={})", dto);
//		log.info("update(orderId={}, dto={})", orderId, dto);
		// orderService.update(dto);
		orderService.update(orderId, dto);
		// return "redirect:/mypage/store/orderDetail";
		return ResponseEntity.ok("Success");
	}
	
}

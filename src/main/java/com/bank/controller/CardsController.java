package com.bank.controller;

import com.bank.model.Cards;
import com.bank.model.Deposits;
import com.bank.service.CardsService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cards")
public class CardsController {

    private final CardsService cardsService;

    public CardsController(CardsService cardsService) {
        this.cardsService = cardsService;
    }



    @PostMapping("/apply/{userId}")
    public String applyCard(@PathVariable Integer userId, @RequestBody Map<String, Object> request) {
        Cards.CardType type = Cards.CardType.valueOf(request.get("cardType").toString().toUpperCase());
        BigDecimal limit = new BigDecimal(request.get("limit").toString());
        return cardsService.applyCard(userId, type, limit);
    }

    @PutMapping("/request-block/{userId}/{cardId}")
    public String requestBlockCard(@PathVariable Integer userId, @PathVariable Long cardId) {
        return cardsService.requestBlockCard(cardId, userId);
    }

    @PutMapping("/request-delete/{userId}/{cardId}")
    public String requestDeleteCard(@PathVariable Integer userId, @PathVariable Long cardId) {
        return cardsService.requestDeleteCard(cardId, userId);
    }

    @GetMapping("/user/{userId}")
    public List<Cards> getUserCards(@PathVariable Integer userId) {
        return cardsService.getUserCards(userId);
    }


    @PutMapping("/admin/approve/{adminId}/{cardId}")
    public String approveCard(@PathVariable Integer adminId, @PathVariable Long cardId) {
        return cardsService.approveCard(cardId, adminId);
    }

    @PutMapping("/admin/reject/{adminId}/{cardId}")
    public String rejectCard(@PathVariable Integer adminId, @PathVariable Long cardId) {
        return cardsService.rejectCard(cardId, adminId);
    }

    @PutMapping("/admin/approve-block/{adminId}/{cardId}")
    public String approveBlock(@PathVariable Integer adminId, @PathVariable Long cardId) {
        return cardsService.approveBlockCard(cardId, adminId);
    }

    @DeleteMapping("/admin/approve-delete/{adminId}/{cardId}")
    public String approveDelete(@PathVariable Integer adminId, @PathVariable Long cardId) {
        return cardsService.approveDeleteCard(cardId, adminId);
    }
    
    @GetMapping("/all/{adminId}")
    public List<Cards> getAllCards(@PathVariable Integer adminId) {
        return cardsService.getAllCards(adminId);
    }
}

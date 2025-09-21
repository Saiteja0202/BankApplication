package com.bank.service;

import com.bank.model.Cards;

import java.math.BigDecimal;
import java.util.List;

public interface CardsService {

    String applyCard(Integer userId, Cards.CardType type, BigDecimal limit);
    String requestBlockCard(Long cardId, Integer userId);
    String requestDeleteCard(Long cardId, Integer userId);

    String approveCard(Long cardId, Integer adminId);
    String rejectCard(Long cardId, Integer adminId);
    String approveBlockCard(Long cardId, Integer adminId);
    String approveDeleteCard(Long cardId, Integer adminId);

    List<Cards> getUserCards(Integer userId);
}

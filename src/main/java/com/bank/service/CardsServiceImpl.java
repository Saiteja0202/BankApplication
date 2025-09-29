package com.bank.service;

import com.bank.exception.CardNotFoundException;
import com.bank.exception.UserNotFoundException;
import com.bank.model.*;
import com.bank.model.Transactions.RequestStatus;
import com.bank.repository.*;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardsServiceImpl implements CardsService {

    private final CardsRepo cardsRepo;
    private final UsersRepo usersRepo;
    private final AccountsRepo accountsRepo;
    private final TransactionsRepo transactionsRepo;

    public CardsServiceImpl(CardsRepo cardsRepo, UsersRepo usersRepo,
                            AccountsRepo accountsRepo, TransactionsRepo transactionsRepo) {
        this.cardsRepo = cardsRepo;
        this.usersRepo = usersRepo;
        this.accountsRepo = accountsRepo;
        this.transactionsRepo = transactionsRepo;
    }

    @Override
    public String applyCard(Integer userId, Cards.CardType type, BigDecimal limit) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Cards card = new Cards();
        card.setUser(user);
        card.setCardType(type);
        card.setLimitAmount(limit);
        card.setRequestStatus(Cards.RequestStatus.PENDING);

        cardsRepo.save(card);

        return "Card request submitted. Awaiting admin approval.";
    }

    @Override
    public String requestBlockCard(Long cardId, Integer userId) {
        Cards card = getUserCard(cardId, userId);
        card.setRequestStatus(Cards.RequestStatus.PENDING);
        cardsRepo.save(card);
        return "Block request submitted. Awaiting admin approval.";
    }

    @Override
    public String requestDeleteCard(Long cardId, Integer userId) {
        Cards card = getUserCard(cardId, userId);
        card.setRequestStatus(Cards.RequestStatus.PENDING);
        cardsRepo.save(card);
        return "Delete request submitted. Awaiting admin approval.";
    }

    @Override
    public String approveCard(Long cardId, Integer adminId) {
        checkAdmin(adminId);

        Cards card = getCard(cardId);
        if (card.getRequestStatus() != Cards.RequestStatus.PENDING) {
            return "Card is not in pending state!";
        }

        String cardNumber = generateUniqueCardNumber();
        card.setCardNumber(cardNumber);
        card.setStatus(Cards.CardStatus.ACTIVE);
        card.setRequestStatus(Cards.RequestStatus.APPROVED);
        cardsRepo.save(card);

        Accounts account = accountsRepo.findFirstByUser(card.getUser())
                .orElseThrow(() -> new UserNotFoundException("Account not found for user"));
        recordTransaction(account, BigDecimal.ZERO, Transactions.TransactionType.CARD_PAYMENT,
                "CARD-ISSUE-" + cardNumber);

        return "Card approved and issued: " + cardNumber;
    }

    @Override
    public String rejectCard(Long cardId, Integer adminId) {
        checkAdmin(adminId);

        Cards card = getCard(cardId);
        card.setRequestStatus(Cards.RequestStatus.REJECTED);
        card.setStatus(null);
        card.setCardNumber(null);
        cardsRepo.save(card);

        return "Card request rejected. Customer may reapply.";
    }

    @Override
    public String approveBlockCard(Long cardId, Integer adminId) {
        checkAdmin(adminId);

        Cards card = getCard(cardId);
        card.setStatus(Cards.CardStatus.BLOCKED);
        card.setRequestStatus(Cards.RequestStatus.APPROVED);
        cardsRepo.save(card);

        Accounts account = accountsRepo.findFirstByUser(card.getUser())
                .orElseThrow(() -> new UserNotFoundException("Account not found for user"));
        recordTransaction(account, BigDecimal.ZERO, Transactions.TransactionType.CARD_PAYMENT,
                "CARD-BLOCK-" + card.getCardNumber());

        return "Card " + card.getCardNumber() + " has been blocked.";
    }

    @Override
    public String approveDeleteCard(Long cardId, Integer adminId) {
        checkAdmin(adminId);

        Cards card = getCard(cardId);
        Accounts account = accountsRepo.findFirstByUser(card.getUser())
                .orElseThrow(() -> new UserNotFoundException("Account not found for user"));

        recordTransaction(account, BigDecimal.ZERO, Transactions.TransactionType.CARD_PAYMENT,
                "CARD-DELETE-" + card.getCardNumber());

        cardsRepo.delete(card);
        return "Card deleted after admin approval.";
    }

    @Override
    public List<Cards> getUserCards(Integer userId) {
        return cardsRepo.findByUserId(userId);
    }

    private Cards getUserCard(Long cardId, Integer userId) {
        Cards card = getCard(cardId);
        if (card.getUser().getUser_id() != userId) {
            throw new CardNotFoundException("Card does not belong to this user");
        }
        return card;
    }

    private Cards getCard(Long cardId) {
        return cardsRepo.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    private void checkAdmin(Integer adminId) {
        Users admin = usersRepo.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (admin.getUserRole() != Users.UserRole.ADMIN) {
            throw new SecurityException("Only ADMIN can perform this action");
        }
    }

    private String generateUniqueCardNumber() {
        SecureRandom random = new SecureRandom();
        String cardNumber;
        do {
            cardNumber = String.format("%016d", Math.abs(random.nextLong() % 1_0000_0000_0000_0000L));
        } while (cardsRepo.findByCardNumber(cardNumber).isPresent());
        return cardNumber;
    }

    private void recordTransaction(Accounts account, BigDecimal amount,
                                   Transactions.TransactionType type, String beneficiaryAccount) {
        Transactions tx = new Transactions();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        tx.setType(type);
        tx.setBeneficiaryAccount(beneficiaryAccount);
        tx.setRequestStatus(RequestStatus.PENDING);
        transactionsRepo.save(tx);
    }
    
    
    @Override
    public List<Cards> getAllCards(Integer adminId) {
    	checkAdmin(adminId);
        return cardsRepo.findAll();
    }

}

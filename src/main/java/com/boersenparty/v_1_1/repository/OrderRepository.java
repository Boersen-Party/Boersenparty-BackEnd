package com.boersenparty.v_1_1.repository;

import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.models.PartyGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {
    //Die Orders eines bestimmten Gastes, einer bestimmten Party
    List<Order> findByPartyIdAndPartyGuestId(Long partyId, Long partyGuestId);
    List<Order> findByPartyId(Long partyId);

    @Query("SELECT o FROM Order o WHERE o.partyGuest.uuid = :uuid")
    List<Order> findByPartyGuestUuid(@Param("uuid") UUID uuid);
    @Query("SELECT o FROM Order o JOIN FETCH o.partyGuest WHERE o.expires_at < :expiresAt")
    List<Order> findByExpiresAtBefore(@Param("expiresAt") LocalDateTime expiresAt);

    @Query("SELECT o FROM Order o WHERE o.partyGuest = :partyGuest AND o.is_paid = false AND o.expires_at > :expiresAt")
    Order findFirstByPartyGuestAndIsPaidFalseAndExpiresAtBefore(@Param("partyGuest") PartyGuest partyGuest, @Param("expiresAt") LocalDateTime expiresAt);







}

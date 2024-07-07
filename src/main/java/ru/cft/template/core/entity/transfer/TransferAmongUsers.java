package ru.cft.template.core.entity.transfer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cft.template.core.entity.Wallet;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "wallet", name = "transfers_among_users")
public class TransferAmongUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_senderId", nullable = false)
    private Wallet senderWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_recipientId", nullable = false)
    private Wallet recipientWallet;

    @Column(name = "c_amount", nullable = false)
    private Long amount;

    @Column(name = "c_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    @Column(name = "c_dateTime", nullable = false)
    private LocalDateTime dateTime;

    @Transient
    private String recipientPhoneNumber = null;
    @Transient
    private String recipientEmail = null;

    public TransferAmongUsers(Long id, Wallet senderWallet, Wallet recipientWallet, Long amount, TransferStatus status, LocalDateTime dateTime) {
        this.id = id;
        this.senderWallet = senderWallet;
        this.recipientWallet = recipientWallet;
        this.amount = amount;
        this.status = status;
        this.dateTime = dateTime;
    }
}

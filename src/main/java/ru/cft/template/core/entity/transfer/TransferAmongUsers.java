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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cft.template.core.entity.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "wallet", name = "transfers_among_users")
public class TransferAmongUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_senderId", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_recipientId", nullable = false)
    private User recipient;

    @Column(name = "c_amount", nullable = false)
    private Integer amount;

    @Column(name = "c_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    @Column(name = "c_dateTime", nullable = false)
    private LocalDateTime dateTime;
}

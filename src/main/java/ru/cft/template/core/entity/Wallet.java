package ru.cft.template.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(schema = "wallet", name = "wallets")
public class Wallet {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    @Column(name = "c_balance", columnDefinition = "integer default 100")
    @NotNull
    private Long balance;

    @Transient
    private boolean hesoyamWinner = false;

    @Transient
    private final Long hesoyamWinningAmount = 10L;

    public Wallet(Long id, User user, Long balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }
}

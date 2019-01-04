package de.adesso.budgeteer.persistence.contract;

import de.adesso.budgeteer.persistence.user.UserEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CONTRACT_SORTING")
public class ContractSortingEntity {
    @Id
    @SequenceGenerator(name = "SEQ_CONTRACT_SORTING_ID", sequenceName = "SEQ_CONTRACT_SORTING_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONTRACT_SORTING_ID")
    private long id;

    @Column(name = "SORTING_INDEX")
    private Integer sortingIndex;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "CONTRACT_ID")
    private ContractEntity contract;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;
}
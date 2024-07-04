package ru.cft.template.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "wallet", name = "t_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_firstName")
    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[À-ß¨][à-ÿ¸]{1,50}$")
    private String firstName;

    @Column(name = "c_middleName")
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[À-ß¨][à-ÿ¸]{1,50}$")
    private String middleName;

    @Column(name = "c_lastName")
    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[À-ß¨][à-ÿ¸]{1,50}$")
    private String lastName;

    @Column(name = "c_phoneNumber", unique = true)
    @NotNull
    @Size(min = 11, max = 11)
    @Pattern(regexp = "^7\\d{10}$")
    private String phoneNumber;

    @Column(name = "c_email", unique = true)
    @NotNull
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{wallet.users.create.errors.email_condition_is_invalid}")
    private String email;

    @Column(name = "c_birthdate")
    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}$")
    @DateTimeFormat(pattern = "yyyy.MM.dd", iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthdate;

    @Column(name = "c_passwordHash")
    @NotNull
    private String passwordHash;

    @Column(name = "c_registrationDateTime")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime registrationDateTime;

    @Column(name = "c_lastUpdateDateTime")
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastUpdateDateTime;
}

package com.povilas.eagle_bank.user.persistence;

import com.povilas.eagle_bank.user.domain.Address;
import com.povilas.eagle_bank.user.domain.User;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.name(),
                user.email(),
                user.phoneNumber(),
                user.address().line1(),
                user.address().line2(),
                user.address().line3(),
                user.address().town(),
                user.address().county(),
                user.address().postcode(),
                Timestamp.from(user.createdTimestamp()),
                Timestamp.from(user.updatedTimestamp())
        );
    }

    public User toDomain(UserEntity entity) {
        Address address = new Address(
                entity.addressLine1(),
                entity.addressLine2(),
                entity.addressLine3(),
                entity.addressTown(),
                entity.addressCounty(),
                entity.addressPostcode()
        );
        return new User(
                entity.id(),
                entity.name(),
                address,
                entity.phoneNumber(),
                entity.email(),
                entity.createdTimestamp().toInstant(),
                entity.updatedTimestamp().toInstant()
        );
    }
}

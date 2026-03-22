package com.povilas.eagle_bank.user.persistence;

import com.povilas.eagle_bank.user.domain.Address;
import com.povilas.eagle_bank.user.domain.User;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public UserEntity toEntity(ResultSet rs) throws SQLException {
        return new UserEntity(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("address_line1"),
                rs.getString("address_line2"),
                rs.getString("address_line3"),
                rs.getString("address_town"),
                rs.getString("address_county"),
                rs.getString("address_postcode"),
                rs.getTimestamp("created_timestamp"),
                rs.getTimestamp("updated_timestamp")
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

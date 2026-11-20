package bac.ac.libis.service;

import bac.ac.libis.dao.MemberDao;
import bac.ac.libis.model.Member;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class MemberService {
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private final MemberDao dao = new MemberDao();

    public Member create(Member member) throws ValidationException, SQLException {
        validate(member, null, true);
        dao.create(member);
        return member;
    }

    public Member update(Member member) throws ValidationException, SQLException {
        validate(member, member.getMemberId(), false);
        dao.update(member);
        return member;
    }

    public void delete(int memberId, int signedInMemberId) throws ValidationException, SQLException {
        if (memberId == signedInMemberId) {
            throw new ValidationException(java.util.Map.of("delete", "You cannot delete the account you are currently signed in with."));
        }
        dao.delete(memberId);
    }

    private void validate(Member member, Integer excludedId, boolean creating) throws ValidationException, SQLException {
        ValidationException errors = new ValidationException();
        if (member.getFullNameText() == null || member.getFullNameText().trim().isEmpty())
            errors.addError("fullName", "Full name is required.");
        if (member.getEmail() == null || member.getEmail().trim().isEmpty())
            errors.addError("email", "Email is required.");
        else if (!EMAIL.matcher(member.getEmail().trim()).matches())
            errors.addError("email", "Enter a valid email address.");
        if (creating && (member.getPassword() == null || member.getPassword().isBlank()))
            errors.addError("password", "Password is required when creating a member.");

        if (member.getEmail() != null && !member.getEmail().trim().isEmpty()) {
            Member duplicate = dao.findByEmail(member.getEmail());
            if (duplicate != null && (excludedId == null || duplicate.getMemberId() != excludedId))
                errors.addError("email", "Email address is already used.");
        }
        if (errors.hasErrors()) throw errors;
    }
}

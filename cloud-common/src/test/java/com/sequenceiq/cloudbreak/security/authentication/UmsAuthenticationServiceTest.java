package com.sequenceiq.cloudbreak.security.authentication;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cloudera.thunderhead.service.usermanagement.UserManagementProto;
import com.sequenceiq.cloudbreak.auth.altus.GrpcUmsClient;
import com.sequenceiq.cloudbreak.auth.altus.exception.UmsAuthenticationException;
import com.sequenceiq.cloudbreak.common.user.CloudbreakUser;

@RunWith(MockitoJUnitRunner.class)
public class UmsAuthenticationServiceTest {

    @Mock
    private GrpcUmsClient grpcUmsClient;

    private UmsAuthenticationService underTest;

    @Before
    public void before() {
        underTest = new UmsAuthenticationService(grpcUmsClient);
    }

    @Test(expected = UmsAuthenticationException.class)
    public void testInvalidCrn() {
        try {
            underTest.getCloudbreakUser("crsdfadsfdsf sadasf3-df81ae585e10", "principal");
        } catch (UmsAuthenticationException e) {
            assertEquals("Invalid CRN has been provided: crsdfadsfdsf sadasf3-df81ae585e10", e.getMessage());
            throw e;
        }
    }

    @Test(expected = UmsAuthenticationException.class)
    public void testInvalidTypeCrn() {
        String crn = "crn:altus:iam:us-west-1:9d74eee4-1cad-45d7-b645-7ccf9edbb73d:cluster:qaas/b8a64902-7765-4ddd-a4f3-df81ae585e10";
        try {
            underTest.getCloudbreakUser(crn, "principal");
        } catch (UmsAuthenticationException e) {
            assertEquals("Authentication is supported only with User and MachineUser CRN: " + crn, e.getMessage());
            throw e;
        }
    }

    @Test
    public void testUserCrn() {
        String crn = "crn:altus:iam:us-west-1:9d74eee4-1cad-45d7-b645-7ccf9edbb73d:user:qaas/b8a64902-7765-4ddd-a4f3-df81ae585e10";
        UserManagementProto.User user = UserManagementProto.User.newBuilder()
                .setUserId("userId")
                .setEmail("e@mail.com")
                .setCrn(crn)
                .build();

        when(grpcUmsClient.getUserDetails(anyString(), anyString(), any())).thenReturn(user);
        CloudbreakUser cloudbreakUser = underTest.getCloudbreakUser(crn, null);

        assertEquals("userId", cloudbreakUser.getUserId());
        assertEquals("e@mail.com", cloudbreakUser.getUsername());
        assertEquals(crn, cloudbreakUser.getUserCrn());
    }

    @Test
    public void testMachineUserCrn() {
        String crn = "crn:altus:iam:us-west-1:9d74eee4-1cad-45d7-b645-7ccf9edbb73d:machineUser:qaas/b8a64902-7765-4ddd-a4f3-df81ae585e10";
        UserManagementProto.MachineUser machineUser = UserManagementProto.MachineUser.newBuilder()
                .setMachineUserId("machineUserId")
                .setCrn(crn)
                .build();

        when(grpcUmsClient.getMachineUserDetails(anyString(), anyString(), any())).thenReturn(machineUser);
        CloudbreakUser cloudbreakUser = underTest.getCloudbreakUser(crn, "principal");

        assertEquals("machineUserId", cloudbreakUser.getUserId());
        assertEquals("principal", cloudbreakUser.getUsername());
        assertEquals(crn, cloudbreakUser.getUserCrn());
    }
}

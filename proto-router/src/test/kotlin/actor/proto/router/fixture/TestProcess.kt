package actor.proto.router.fixture

import actor.proto.PID
import actor.proto.mailbox.SystemMessage

class TestProcess : actor.proto.Process() {
    override fun sendUserMessage(pid: PID, message: Any) {
    }

    override fun sendSystemMessage(pid: PID, message: SystemMessage) {
    }
}


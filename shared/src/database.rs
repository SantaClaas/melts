use crux_core::capability::{CapabilityContext, Operation};
use crux_core::macros::Capability;
use serde::{Deserialize, Serialize};

// use crux_core::capability::OP
#[derive(Serialize, Deserialize, PartialEq)]
pub struct GetUserOperation;

// impl Operation
impl Operation for GetUserOperation {
    type Output = String;
}

#[derive(Capability)]
pub struct Database<Event> {
    context: CapabilityContext<GetUserOperation, Event>,
}

impl<Event> Database<Event> {
    pub fn new(context: CapabilityContext<GetUserOperation, Event>) -> Self {
        Self { context }
    }

    pub fn get_user(&self) -> String {

        // let context = self.context.clone();
        // self.context.spawn(async move {
        //     // context.request_from_shell(GetUserOperation).await;
        //
        // });
        //
        todo!()
    }
}
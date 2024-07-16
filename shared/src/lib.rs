pub mod app;
pub mod database;

use std::sync::OnceLock;

pub use crux_core::{bridge::Bridge, Core, Request};

pub use app::*;


uniffi::include_scaffolding!("shared");

fn core() -> &'static Bridge<Effect, Counter> {
    static CORE: OnceLock<Bridge<Effect, Counter>> = OnceLock::new();
    CORE.get_or_init(|| Bridge::new(Core::new()))
}

pub fn process_event(data: &[u8]) -> Vec<u8> {
    core().process_event(data)
}

pub fn handle_response(id: u32, data: &[u8]) -> Vec<u8> {
    core().handle_response(id, data)
}

pub fn view() -> Vec<u8> {
    core().view()
}

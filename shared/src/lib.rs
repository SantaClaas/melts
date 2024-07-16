pub mod app;
pub mod database;

use std::sync::OnceLock;

pub use app::*;


uniffi::include_scaffolding!("shared");

struct Counter {
    count: isize,
}

fn core() -> &'static Counter {
    static CORE: OnceLock<Counter> = OnceLock::new();
    CORE.get_or_init(|| Counter { count: 0 })
}

fn increment() -> i32 {
    3
}
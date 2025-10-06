create table customers (
  id uuid primary key,
  name varchar(120) not null,
  email varchar(160) not null unique,
  created_at timestamptz not null default now()
);

create table products (
  id uuid primary key,
  name varchar(140) not null,
  sku varchar(64) not null unique,
  price_cents bigint not null check (price_cents >= 0),
  created_at timestamptz not null default now()
);

create table orders (
  id uuid primary key,
  customer_id uuid not null references customers(id),
  status varchar(24) not null,
  total_cents bigint not null check (total_cents >= 0),
  created_at timestamptz not null default now()
);

create table order_items (
  id uuid primary key,
  order_id uuid not null references orders(id) on delete cascade,
  product_id uuid not null references products(id),
  quantity int not null check (quantity > 0),
  price_cents bigint not null check (price_cents >= 0)
);

create index idx_orders_customer on orders(customer_id);
create index idx_order_items_order on order_items(order_id);